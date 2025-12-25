package com.demo.backend.service.impl;

import com.demo.backend.dto.ai.AIChatRequest;
import com.demo.backend.dto.ai.AIChatResponse;
import com.demo.backend.mapper.ProductMapper;
import com.demo.backend.model.Product;
import com.demo.backend.service.AIChatService;
import com.demo.backend.service.OpenAIService;
import com.demo.backend.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIChatServiceImpl implements AIChatService {

    private final ProductSearchService productSearchService;
    private final ProductMapper productMapper;
    private final OpenAIService openAIService;

    @Override
    public AIChatResponse process(AIChatRequest req) {

        // Search relevant products
        List<Product> matches = productSearchService.searchRelevantProducts(req.getMessage());

        // Build product context for AI
        StringBuilder ctx = new StringBuilder();
        for (Product p : matches) {

            String categoryName =
                    (p.getCategory() != null && p.getCategory().getName() != null)
                            ? p.getCategory().getName()
                            : "Unknown Category";

            String desc =
                    (p.getShortDescription() != null)
                            ? p.getShortDescription()
                            : "No description available.";

            ctx.append("Product:\n");
            ctx.append("- Name: ").append(p.getName()).append("\n");
            ctx.append("- Price: ").append(p.getPrice()).append("\n");
            ctx.append("- Category: ").append(categoryName).append("\n");
            ctx.append("- Description: ").append(desc).append("\n\n");
        }

        // AI prompt
        String prompt =
                "You are an intelligent Apple Store shopping assistant.\n\n" +
                        "User message: \"" + req.getMessage() + "\"\n\n" +
                        "Relevant product data:\n" + ctx +
                        "\nRespond professionally:\n" +
                        "- Recommend products if appropriate\n" +
                        "- Compare items when useful\n" +
                        "- Explain reasons clearly\n" +
                        "- Be friendly and helpful\n";

        // Call AI
        String reply = openAIService.ask(prompt);

        return AIChatResponse.builder()
                .reply(reply)
                .recommendations(
                        matches.stream()
                                .limit(3)
                                .map(productMapper::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
