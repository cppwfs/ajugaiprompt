package io.spring.ajugai;


import org.springframework.ai.client.AiClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LightningTalkController {

    private final AiClient aiClient;

    @Value("classpath:/prompts/prompt.txt")
    private Resource qaPromptResource;

    @Autowired
    public LightningTalkController(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    @GetMapping("/basic")
    public Completion getBasicTalk(String question) {
        System.out.println("Generating Answer");
        Completion completion = new Completion(aiClient.generate(question));
        System.out.println(completion.getCompletion());
        return completion;
    }

    @GetMapping("/pro")
    public String getTalkWithExample(String question) {
        System.out.println("Generating Answer");
        Map<String, Object> map = new HashMap<>();
        map.put("question", question);
        PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource, map);
        Prompt prompt = promptTemplate.create(map);
        Completion completion = new Completion(aiClient.generate(prompt).getGeneration().getText());
        System.out.println(completion.getCompletion());
        return completion.getCompletion();
    }

}
