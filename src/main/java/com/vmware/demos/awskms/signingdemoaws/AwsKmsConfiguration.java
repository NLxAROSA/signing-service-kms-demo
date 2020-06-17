package com.vmware.demos.awskms.signingdemoaws;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
public class AwsKmsConfiguration {
    
    @Bean
	public KmsClient kmsClient()	{
        return KmsClient.builder().build();
    }
    


}