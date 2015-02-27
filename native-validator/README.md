# native-validator

This project provides a simple API that can be used to validate JSON requests and responses according to Native specifications. Native version 1.0 is fully supported.

Provided under the New BSD License. Refer to the file LICENSE file in the root of this project for more information.

## Usage

Add the native-validator dependency to your Maven pom.xml:

    <dependency>
        <groupId>org.openrtb</groupId>
        <artifactId>native-validator</artifactId>
        <version>1.0.0</version>
        <type>jar</type>
    </dependency>

To ascertain whether a given JSON String, File, Reader, or Resource is a valid request according to Native v1.0 specifications:

    NativeValidator validator = NativeValidatorFactory.getValidator(NativeInputType.REQUEST, NativeVersion.V1_0);
    boolean valid = validator.isValid(json);

To get a detailed validation report including reasons why the JSON is invalid:

    ValidationResult validationResult = validator.validate(json);
    System.out.println("valid: " + validationResult.isValid() + ", result: " + validationResult.getResult());

## Specification Documents

The specification documents used to create these Native validation schemas can be found under src/main/resources/specification and at http://www.iab.net/guidelines/rtbproject.
