package org.egov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Component
public class TestDataFormatter {

    @Autowired
    private TestProperties testProperties;

    private ObjectMapper objectMapper;

    public TestDataFormatter() {
        objectMapper = new ObjectMapper();
    }

    /**
     * @param fileName
     * @return
     */
    private File getFileFromClassLoaderResource(String fileName) {
        String basePackage = testProperties.getTestDataPackage();
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (basePackage != null && !basePackage.isEmpty()) {
            String baseURL = classLoader.getResource(basePackage).getFile();
            fileName = baseURL + File.separator + fileName;

            return new File(fileName);
        } else {
            return new File(classLoader.getResource(fileName).getFile());
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public String getPspclBillRequestData() throws IOException {
        return (Files.lines(Paths.get(getFileFromClassLoaderResource(testProperties.getPspclBillRequest()).getPath()))
                .collect(Collectors.joining("\n")));
    }

    /**
     * @return
     * @throws IOException
     */
    public String getPspclPaymentRequestData() throws IOException {
        return (Files.lines(Paths.get(getFileFromClassLoaderResource(testProperties.getPspclPaymentRequest()).getPath()))
                .collect(Collectors.joining("\n")));
    }

    /**
     * @return
     * @throws IOException
     */
    public String getPspclBillResponseData() throws IOException {
        return (Files.lines(Paths.get(getFileFromClassLoaderResource(testProperties.getPspclBillResponse()).getPath()))
                .collect(Collectors.joining("\n")));
    }

    /**
     * @return
     * @throws IOException
     */
    public String getPspclPaymentResponseData() throws IOException {
        return (Files.lines(Paths.get(getFileFromClassLoaderResource(testProperties.getPspclPaymentResponse()).getPath()))
                .collect(Collectors.joining("\n")));
    }
}
