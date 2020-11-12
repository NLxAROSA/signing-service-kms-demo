# AWS KMS Signing demo

1. Create an IAM user/role in AWS for the purpose of this demo with the proper permissions
2. Add a key in the users security credentials and put it somewhere safe (this will be used to log in)
3. Setup your local AWS CLI using `aws configure` (if you logged in before just hit enter a couple of times as it will remember your credentials, otherwise enter key id, secret and desired default region)
4. Create a key using `aws kms create-key --key-usage SIGN_VERIFY --customer-master-key-spec ECC_NIST_P256 --region <yourfavoriteregion>` (Make sure the region is the same as the desired default region). Take note of the key id.
5. Update the policy on this key so your IAM user can operate on it. Note: you can't do that with the your freshly created IAM user because it has no rights on this key. You can get the existing policy out by using `aws kms get-key-policy --key-id <idofyourgeneratedkeyhere> --policy-name default`. Add a block in the `Statement` array for your newly created IAM user such as the one below in the JSON in the Statement array (don't remove the existing one as that will lock you out of the key). Only add the `kms:Sign` and `kms:Verify` actions as we don't want this user to be able to do anything else. Update the policy by using `aws kms put-key-policy --key-id <idofyourgeneratedkeyhere> --policy-name default --policy <completejsonofthepolicy>`. Your complete JSON should look like the one below, with one block that was already there (for your root user, which allows all actions in KMS on this key) and one block that you added for your newly created IAM user (only Sign and Verify).

```json
{
    "Version": "2012-10-17",
    "Id": "key-default-1",
    "Statement": [
        {
            "Sid": "Enable IAM User Permissions",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::123456789012:nameofyourrootuser"
            },
            "Action": "kms:*",
            "Resource": "*"
        },
        {
            "Sid": "Enable IAM User Permissions for sub user",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::210987654321:user/nameofyournewlycreatediamuser"
            },
            "Action": [
                "kms:Sign",
                "kms:Verify"
            ],
            "Resource": "*"
        }
    ]
}
```

6. The default client will figure out the connection settings by itself when running locally, but if you're not running locally, set your `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` and `AWS_REGION` environment variables so the applicaton can connect to AWS.
7. Export the id of your created key into the environment variable keyid using `export keyid="yourkeyidinhere"`. If you forgot the id of the key you can list your keys using `aws kms list-keys`.
8. `mvnw spring-boot:run` the application
9. Great success!