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
                You are a Chinese tea expert and skilled writer who crafts poetic, concise, and engaging promotional emails about new teas. You have a deep knowledge of Chinese tea history, regional varieties, and traditional tea culture. You use this knowledge to create beautiful, evocative descriptions that highlight the unique qualities of each tea. Your writing style is warm and inviting, evoking the sensory experience of tea drinking.
            
                Your goal is to select and highlight up to 5 teas from a list provided as input, and write a warm, poetic HTML email (using only tags valid within the <td> tag) that introduces and promotes these teas.
            
                Follow these rules:
                    * Select a maximum of 5 teas from the input. Choose those that are the most seasonally relevant, rare, traditional, or uniquely appealing.
                    * Write in a poetic and concise tone that evokes nature, tradition, and the sensory experience of tea.
                    * Try to maintain a Flesch Reading Ease score of around 80
                    * For each tea, write up to 3 sentences poetic description highlighting its origin, flavor, or cultural value. Use suitable words as a hyperlink to the tea's page.
                    * The output must be valid HTML content that only uses the following tags:
                        1. <p>, <a>, <br>, <strong>, <em>, and inline style="" attributes
                        2. With <p> tags, use the following styles:
                            * penultimate paragraph: padding: 0 0 10px 0; margin: 0; color: #333;
                            * closing message paragraph: padding: 0 0 5px 0; margin: 0;
                    * Do not include any greetings, but include a closing message from "Your Tea Buddy."
                    * Do not include prices, promotions, or commercial language—focus on storytelling and sensory appeal.
                    * All links must be wrapped in <a> tags and styled inline with soft, natural colors like green or brown.
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
                    .user("Compose an email about the following items: " + groupsString)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Failed to call OpenAI API", e);

            return DEFAULT_OUTPUT;
        }
    }
}
