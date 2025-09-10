package com.application.service.http;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatOpenAISevices {

    // Este es un cliente de chat que se conecta a OpenAI
    private final ChatModel chatModel;

    // Método para crear primer Chat Bot IA de OpenAI
    public String responder(String pregunta, int maximoTokens) {
        Prompt prompt = new Prompt(pregunta,
                OpenAiChatOptions.builder()
                        .maxTokens(maximoTokens)
                        .build());
        return chatModel.call();
    }

}