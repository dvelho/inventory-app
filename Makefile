# Deploy sam cognito tenant-auth-config.yaml
cognito-deploy-dev-minka-tech:
	sam build --template-file infrastructure/tenant-auth-config.yaml \
	&& \
	sam deploy --template-file infrastructure/tenant-auth-config.yaml \
	--stack-name sam-cognito-tenant-minka-tech-dev-config \
	--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
	--region eu-west-1 \
	--parameter-overrides \
	TenantName=minka-tech \
	GeneralDomain=minka.tech \
	HostedZoneId=Z012886125XSYKBOI8V6M \
	CognitoDomain=auth.minka.tech \
	CertificateArn=arn:aws:acm:us-east-1:631674088803:certificate/34706a68-bf88-41f0-8f6a-d9646b263404 \
	Environment=dev
cognito-deploy-delete-dev-tech:
	sam delete --stack-name sam-cognito-tenant-minka-tech-dev-config

cognito-deploy-dev-minka-cloud-validade:
	sam validate --template-file infrastructure/tenant-auth-config.yaml \

cognito-deploy-dev-minka-cloud:
	sam build --template-file infrastructure/tenant-auth-config.yaml \
	&& \
	sam deploy --template-file infrastructure/tenant-auth-config.yaml \
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
cognito-delete-dev-minka-cloud:
	sam delete --stack-name sam-cognito-tenant-minka-cloud-dev-config

cognito-deploy-prod-minka-cloud:
	sam build --template-file infrastructure/tenant-auth-config.yaml \
	&& \
	sam deploy --template-file infrastructure/tenant-auth-config.yaml \
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
	Environment=prod1
cognito-delete-prod-minka-cloud:
	sam delete --stack-name sam-cognito-tenant-minka-cloud-prod-config



cognito-deploy-delete-test:
	sam delete --stack-name sam-cognito-tenant-config

maven-release:
	mvn update-versions -DautoVersionSubmodules=true \
 	&& mvn release:prepare -DautoVersionSubmodules=true \
 	&& mvn release:perform -DautoVersionSubmodules=true \

install-nx:
	yarn global add nx

build-presignup:
	 mvn -f product/service/lambda-services/cognito/signup/pom.xml -q  install -Dnative -DskipTests
