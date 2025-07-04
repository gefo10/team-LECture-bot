package com.lecturebot.service.genai;

import com.lecturebot.generated.model.IndexRequest;
import com.lecturebot.generated.model.IndexResponse;
import com.lecturebot.generated.model.QueryRequest;
import com.lecturebot.generated.model.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GenAiClient {

    private static final Logger logger = LoggerFactory.getLogger(GenAiClient.class);
    private final RestTemplate restTemplate;
    private final String genAiServiceBaseUrl;

    public GenAiClient(RestTemplate restTemplate, @Value("${GENAI_SERVICE_BASE_URL}") String genAiServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.genAiServiceBaseUrl = genAiServiceBaseUrl;
    }

    public Optional<IndexResponse> indexDocument(IndexRequest request) {
        String url = genAiServiceBaseUrl + "/api/v1/index/index";
        try {
            logger.info("Sending indexing request to GenAI service for documentId: {}", request.getDocumentId());
            IndexResponse response = restTemplate.postForObject(url, request, IndexResponse.class);
            logger.info("Successfully received indexing response from GenAI service.");
            return Optional.ofNullable(response);
        } catch (RestClientException e) {
            logger.error("Error calling GenAI indexing service at {}: {}", url, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<QueryResponse> submitQuery(QueryRequest request) {
        String url = genAiServiceBaseUrl + "/api/v1/query/query";
        try {
            logger.info("Sending query to GenAI service for courseSpaceId: {}", request.getCourseSpaceId());
            QueryResponse response = restTemplate.postForObject(url, request, QueryResponse.class);
            logger.info("Successfully received query response from GenAI service.");
            return Optional.ofNullable(response);
        } catch (RestClientException e) {
            logger.error("Error calling GenAI query service at {}: {}", url, e.getMessage());
            return Optional.empty();
        }
    }
}