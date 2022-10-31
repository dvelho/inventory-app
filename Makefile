# Deploy sam cognito service-tenant.yaml
tenant-deploy-buckets-minka-cloud:
	sam build --template-file infrastructure/deployment-s3-buckets.yaml \
	&& \
	sam deploy --template-file infrastructure/deployment-s3-buckets.yaml \
	--stack-name tenant-deployment-buckets \
	--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
	--region eu-west-1 \
	--resolve-s3 \
	--parameter-overrides \
	TenantName=minka-cloud \

tenant-deploy-delete-buckets-minka-cloud:
	sam delete --stack-name tenant-deployment-buckets

tenant-deploy-dev-minka-cloud-validade:
	sam validate --template-file infrastructure/service-tenant.yaml \

tenant-deploy-dev-minka-cloud:
	sam build --template-file infrastructure/service-tenant.yaml \
	&& \
	sam deploy --template-file infrastructure/service-tenant.yaml \
	--stack-name sam-cognito-tenant-minka-cloud-dev-config \
	--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
	--region eu-west-1 \
	--resolve-s3 \
	--parameter-overrides \
	TenantName=minka-cloud \
	GeneralDomain=minka.cloud \
	HostedZoneId=Z06530912SIRHBPLWQDWO \
	CognitoDomain=auth.minka.cloud \
	CertificateArn=arn:aws:acm:us-east-1:631674088803:certificate/0dfc0ddd-8e43-4662-adad-7b55df9bd9c2 \
	Environment=dev1
tenant-delete-dev-minka-cloud:
	sam delete --stack-name sam-cognito-tenant-minka-cloud-dev-config

tenant-deploy-prod-minka-cloud:
	sam build --template-file infrastructure/service-tenant.yaml \
	&& \
	sam deploy --template-file infrastructure/service-tenant.yaml \
	--stack-name sam-cognito-tenant-minka-cloud-prod-config \
	--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
	--region eu-west-1 \
	--resolve-s3 \
	--parameter-overrides \
	TenantName=minka-cloud \
	GeneralDomain=minka.cloud \
	HostedZoneId=Z06530912SIRHBPLWQDWO \
	CognitoDomain=auth.minka.cloud \
	CertificateArn=arn:aws:acm:us-east-1:631674088803:certificate/0dfc0ddd-8e43-4662-adad-7b55df9bd9c2 \
	Environment=prod1 \
tenant-delete-prod-minka-cloud:
	sam delete --stack-name sam-cognito-tenant-minka-cloud-prod-config


tenant-deploy-delete-test:
	sam delete --stack-name sam-cognito-tenant-config

maven-release:
	mvn update-versions -DautoVersionSubmodules=true \
 	&& mvn release:prepare -DautoVersionSubmodules=true \
 	&& mvn release:perform -DautoVersionSubmodules=true \

install-nx:
	yarn global add nx

build-presignup:
	 mvn -f product/service/lambda-services/tenant/signup/pom.xml -q  install -Dnative -DskipTests



build-user-welcome:
	 mvn -f product/service/lambda-services/user/welcome/pom.xml -q  install -Dnative -DskipTests

# Deploy sam cognito service-tenant.yaml
user-deploy-buckets-minka-cloud-dev:
	sam build --template-file infrastructure/service-user.yaml \
	&& \
	sam deploy --template-file infrastructure/service-user.yaml \
	--stack-name user-minka-cloud-dev \
	--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
	--region eu-west-1 \
	--resolve-s3 \
	--parameter-overrides \
	Environment=dev1 \
	TenantName=minka-cloud \

user-deploy-delete-user-minka-cloud:
	sam delete --stack-name user-minka-cloud-dev-config

