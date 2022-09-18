package chat.api.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class AwsS3Uploader {
    private final String bucket;
    private final AmazonS3Client amazonS3Client;

    public AwsS3Uploader(@Value("${cloud.aws.s3.bucket}") String bucket, AmazonS3Client amazonS3Client) {
        this.bucket = bucket;
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(InputStream inputStream, String originalFilename, String contentType) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + originalFilename;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(inputStream.available());
        objMeta.setContentType(contentType);

        amazonS3Client.putObject(bucket, s3FileName, inputStream, objMeta);

        return amazonS3Client.getUrl(bucket, s3FileName).toString();
    }
}
