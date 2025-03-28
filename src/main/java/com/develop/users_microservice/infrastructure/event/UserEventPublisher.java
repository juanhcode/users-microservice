package com.develop.users_microservice.infrastructure.event;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;

@RestController
@RequestMapping("/publicar")
public class UserEventPublisher {
    private final SnsClient snsClient;

    @Value("${aws.sns.topicArn}")
    private String topicArn;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;


    @Value("${aws.region}")
    private String region;

    public UserEventPublisher(@Value("${aws.region}") String region, @Value("${aws.accessKey}") String accessKey,
                        @Value("${aws.secretKey}") String secretKey) {

        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

    }



    @PostMapping
    public String enviarMensaje(@RequestBody String mensaje) {

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(mensaje)
                .messageGroupId("grupo1")  // Necesario para FIFO
                .messageDeduplicationId(String.valueOf(System.currentTimeMillis())) // Evita duplicados
                .build();

        snsClient.publish(request);
        return "Mensaje publicado en SNS FIFO";
    }
}
