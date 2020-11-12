package com.vmware.demos.awskms.signingdemoaws;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.MessageType;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;
import software.amazon.awssdk.services.kms.model.VerifyRequest;
import software.amazon.awssdk.services.kms.model.VerifyResponse;

@Service
public class SigningService {

    private final KmsClient kmsClient;
    private final String keyId;

    public SigningService(KmsClient awsKms, @Value("${keyid}") String keyId) {
        this.kmsClient = awsKms;
        this.keyId = keyId;
    }

    public byte[] signMessage(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SdkBytes messageSdkBytes = SdkBytes.fromByteArray(message.getBytes("UTF-8"));

        SignRequest signRequest = SignRequest.builder().keyId(keyId).messageType(MessageType.RAW).message(messageSdkBytes)
                .signingAlgorithm(SigningAlgorithmSpec.ECDSA_SHA_256).build();
        SignResponse signResponse = kmsClient.sign(signRequest);

        return signResponse.signature().asByteArray();
    }

    public Boolean isValidSignature(String message, byte[] signature)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        SdkBytes messageSdkBytes = SdkBytes.fromByteArray(message.getBytes("UTF-8"));
        SdkBytes signatureSdkBytes = SdkBytes.fromByteArray(signature);

        VerifyRequest verifyRequest = VerifyRequest.builder().keyId(keyId).messageType(MessageType.RAW)
                .message(messageSdkBytes).signingAlgorithm(SigningAlgorithmSpec.ECDSA_SHA_256).signature(signatureSdkBytes).build();
        VerifyResponse verifyResponse = kmsClient.verify(verifyRequest);

        return verifyResponse.signatureValid();
    }

}