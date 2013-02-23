/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.openrtb.dsp.client;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.util.statemachines.FiniteStateMachine;
import org.openrtb.common.util.statemachines.FSMTransition;
import org.openrtb.common.util.statemachines.FSMCallback;
import org.openrtb.common.util.statemachines.FSMException;
import org.openrtb.dsp.intf.model.DSPConfig;

public class BidRequestTxn {
	private FiniteStateMachine<TSMStates> reqTSM;

	private BidRequest request;	
	private BidResponse response;	
	private Logger logger;
	private DSPConfig config;

	// use type inference to avoid having to type long generic type names
	// *not* pseudo typedefs
	private static <A extends FSMCallback, B> FSMTransition<A, B> newTransition(B event) { 
		return new FSMTransition<A, B>(event);
	}
			
	private static final FSMTransition<TSMStates, String> EV_NEWREQUEST = BidRequestTxn.newTransition("NewRequest");
	private static final FSMTransition<TSMStates, String> EV_FORMATERROR = BidRequestTxn.newTransition("FormatError");
	private static final FSMTransition<TSMStates, String> EV_NOTSUPPORTED = BidRequestTxn.newTransition("NotSupported");
	private static final FSMTransition<TSMStates, String> EV_SELECTBIDS = BidRequestTxn.newTransition("SelectBids");		
	private static final FSMTransition<TSMStates, String> EV_REQUEST_EXPIRED = BidRequestTxn.newTransition("RequestExpired");
	private static final FSMTransition<TSMStates, String> EV_BIDSOFFERED = BidRequestTxn.newTransition("BidsOffered");
	private static final FSMTransition<TSMStates, String> EV_NOMATCHINGBIDS = BidRequestTxn.newTransition("NoMatchingBids");	

	private final Timer requestTimer = new Timer();
	private final TimerTask requestTimerTask = new TimerTask() {
		@Override
		public void run() {
			EV_REQUEST_EXPIRED.setState(reqTSM.getCurrent());
			reqTSM.followTransition(EV_REQUEST_EXPIRED, this);
		}
	};
	
	BidRequestTxn(BidRequest request, Logger logger, DSPConfig config) {
		this.request = request;
		this.logger = logger;
		this.config = config;
		this.response = null;
		this.reqTSM = new FiniteStateMachine<TSMStates>();
		reqTSM.addStates(TSMStates.values());
		reqTSM.addTransition(TSMStates.TXN_CLOSED, EV_NEWREQUEST, TSMStates.TXN_WAIT_NEW);
		reqTSM.addTransition(TSMStates.TXN_WAIT_NEW, EV_FORMATERROR, TSMStates.TXN_FORMATERROR);
		reqTSM.addTransition(TSMStates.TXN_WAIT_NEW, EV_REQUEST_EXPIRED, TSMStates.TXN_REQUESTEXPIRED);
		reqTSM.addTransition(TSMStates.TXN_WAIT_NEW, EV_SELECTBIDS, TSMStates.TXN_WAIT_OPEN);
		reqTSM.addTransition(TSMStates.TXN_WAIT_OPEN, EV_FORMATERROR, TSMStates.TXN_FORMATERROR);
		reqTSM.addTransition(TSMStates.TXN_WAIT_OPEN, EV_REQUEST_EXPIRED, TSMStates.TXN_REQUESTEXPIRED);
		reqTSM.addTransition(TSMStates.TXN_WAIT_OPEN, EV_NOTSUPPORTED, TSMStates.TXN_NOBID);
		reqTSM.addTransition(TSMStates.TXN_WAIT_OPEN, EV_NOMATCHINGBIDS, TSMStates.TXN_NOBID);		
		reqTSM.addTransition(TSMStates.TXN_WAIT_OPEN, EV_BIDSOFFERED, TSMStates.TXN_WAIT_BIDSOFFERED);
	}	
	
	public BidResponse exec() throws RTBMessageException {
		try {
			reqTSM.exec(TSMStates.TXN_CLOSED, this);
			return response;
		} catch (FSMException fe) {
			throw new RTBMessageException(fe.getMessage());
		}
	}

	public enum TSMStates implements FSMCallback {		
		TXN_CLOSED {			
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "New Request");
				// TODO: set Request Timer
				context.setRequestTimer();
				EV_NEWREQUEST.setState(this);
				return EV_NEWREQUEST;
			}

		},
		
		TXN_WAIT_NEW {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Validating Request Message Format");
				boolean valid = context.validateRequest();
				if (!valid) {
					EV_FORMATERROR.setState(this);
					return EV_FORMATERROR;
				}
				EV_SELECTBIDS.setState(this);
				return EV_SELECTBIDS;
			}
		},
		
		TXN_FORMATERROR {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Terminating transaction due to Format Error");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},
		
		TXN_WAIT_OPEN {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Finding matching Bids");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},

		TXN_NOBID {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Terminating transaction due to Format Error");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},

		TXN_WAIT_BIDSOFFERED {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Terminating transaction due to Format Error");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},

		TXN_REQUESTEXPIRED {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Terminating transaction due to Format Error");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},

		TXN_OFFEREXPIRED {
			public FSMTransition<TSMStates, String> exec(Object ctx) throws FSMException {
				BidRequestTxn context = (BidRequestTxn) ctx;
				context.logger.log(Level.INFO, "Terminating transaction due to Format Error");
				context.response = null;
				return null; // this is an end state for this TSM
			}
		}
	}
	
	protected void setRequestTimer() {
		long requestTimeInMs = config.getDefaultRequestTimerMs();
		
		if (request.tmax != null) {
			requestTimeInMs = request.tmax.longValue();
		}
		
		requestTimer.schedule(requestTimerTask, requestTimeInMs);
	}


	protected boolean validateRequest() {
		if (request == null) {
			logger.log(Level.SEVERE, "BidRequest object was null");
			return false;
		} else {
			boolean error = false;
			if (error = (request.id == null))
				logger.log(Level.SEVERE, "BidRequest must have valid Id");
			if (error = ((request.imp == null) || request.imp.isEmpty()))
				logger.log(Level.SEVERE, "BidRequest must have one or more impressions");
			if (error = ((request.site == null) && (request.app == null)))
				logger.log(Level.SEVERE, "BidRequest must have at least site or app object");
			if (error)
				return false;
		}
		return true;
	}

	protected BidRequest getRequestObj() {
		// TODO Auto-generated method stub
		return null;
	}



	public void selectBids() throws RTBMessageException {
		// TODO Auto-generated method stub
		
	}


	public BidResponse getResponse() throws RTBMessageException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
