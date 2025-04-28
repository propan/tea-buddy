package com.github.propan.teabuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.propan.teabuddy.models.ItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailComposer {

    private static final Logger log = LoggerFactory.getLogger(EmailComposer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private final OpenAiChatModel model;

    protected static final String DEFAULT_OUTPUT = """
            <p style="padding: 0 0 5px 0; margin: 0;">I've got some news for you. I just found a few new tea related items you might be interested in!</p>""";

    private static final String SYSTEM_PROMPT = """
        You are a Chinese tea expert with deep knowledge of Chinese history, folklore and traditions. You use this knowledge to write captivating tea descriptions.

        Your task is to select and highlight up to 5 teas from a list provided as input, and write an email introduction that tells an interesting story or a fact about the teas you selected or area they originate from.

        Follow these rules:
            * Select at most 5 teas from the input. Choose those that are the most seasonally relevant, rare, traditional, or uniquely appealing.
            * For each tea, write up to 5 sentences that tell history or a fact about the tea or area it originates from. Use folklore and historical people in the story when possible. Highlight the tea origin, cultural value or history. Use suitable words as a hyperlink to the tea's page. Maintain a Flesch Reading Ease score of around 80
            * The output must be valid HTML content that only uses the following tags:
                1. <p>, <a>, <br>, <strong>, <em>, and inline style="" attributes
                2. With <p> tags, use the following styles:
                    * penultimate paragraph: padding: 0 0 10px 0; margin: 0; color: #333;
                    * closing message paragraph: padding: 0 0 5px 0; margin: 0;
                3. with <a> tags use "color:#333" style
            * Do not include any greetings, but include a closing message from "Your Tea Buddy".
            * Respond with raw HTML. Do not wrap the response in ```html ``` tags.
        """;

    @Autowired
    public EmailComposer(OpenAiChatModel model) {
        this.model = model;
    }

    public String compose(List<ItemGroup> groups) {
        try {
            String groupsString = mapper.writeValueAsString(groups);

            return ChatClient.create(this.model)
                    .prompt()
                    .system(SYSTEM_PROMPT)
                    .user("Write an email about the following items: " + groupsString)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Failed to call OpenAI API", e);

            return DEFAULT_OUTPUT;
        }
    }
}
