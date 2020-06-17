# AWS KMS Signing demo

1. Create an IAM user/role in AWS for the purpose of this demo
2. Setup your local AWS CLI using `aws configure`
3. Create a key using `aws kms create-key --key-usage SIGN_VERIFY --customer-master-key-spec ECC_NIST_P256 --region <yourfavoriteregion>`
4. Set your `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` and `AWS_REGION` the items you created before
5. Change the keyid in the `SigningService` to the KeyId of the created key
6. `mvnw spring-boot:run` the application
7. Great success!