/**
 * 
 */
package org.openrtb.dsp.client;

import org.apache.avro.AvroRemoteException;
import org.openrtb.common.api.BlocklistAPI;
import org.openrtb.common.api.BlocklistRequest;
import org.openrtb.common.api.BlocklistResponse;

/**
 * @author pshroff
 *
 */
public class BlocklistRequesterProxy implements BlocklistAPI {

	/**
	 * 
	 */
	public BlocklistRequesterProxy() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.openrtb.common.api.BlocklistAPI#send(org.openrtb.common.api.BlocklistRequest)
	 */
	@Override
	public BlocklistResponse send(BlocklistRequest blocklistReq) 
												throws AvroRemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
