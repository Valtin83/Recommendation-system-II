package com.example.Recommendation_system.listener;

import com.example.Recommendation_system.model.Recommendation;
import com.example.Recommendation_system.service.RecommendationService;
import com.example.Recommendation_system.service.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final RecommendationService recommendationService;
    private final UserService userService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, RecommendationService recommendationService, UserService userService) {
        this.telegramBot = telegramBot;
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                String text = update.message().text();
                long chatId = update.message().chat().id();

                if ("/start".equals(text)) {
                    sendHelpMessage(chatId);
                } else if (text.startsWith("/recommend ")) {
                    processRecommendCommand(chatId, text.substring(10).trim());
                } else {
                    sendHelpMessage(chatId);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processRecommendCommand(long chatId, String username) {
        try {
            UUID userId = userService.findUserIdByUsername(username);
            if (userId == null) {
                sendMessage(chatId, "Пользователь не найден");
                return;
            }

            List<Recommendation> recommendations = recommendationService.getListRecommendation(userId);
            if (recommendations.isEmpty()) {
                sendMessage(chatId, "Для пользователя " + username + " нет рекомендаций");
                return;
            }

            String userFullName = userService.getUserFullName(userId);
            StringBuilder response = new StringBuilder("Здравствуйте, " + userFullName + "!\n\n");
            response.append("Новые продукты для вас:\n");

            recommendations.forEach(rec -> {
                if (!rec.getDescription().equals("Нет рекомендаций")) {
                    response.append("• ").append(rec.getName()).append(": ")
                            .append(rec.getDescription()).append("\n");
                }
            });

            sendMessage(chatId, response.toString());
        } catch (Exception e) {
            logger.error("Error processing recommend command", e);
            sendMessage(chatId, "Произошла ошибка при обработке запроса");
        }
    }

    private void sendHelpMessage(long chatId) {
        String helpText = "Добро пожаловать! Я бот для рекомендаций продуктов.\n\n" +
                "Используйте команду:\n" +
                "/recommend [имя пользователя] - получить рекомендации";
        sendMessage(chatId, helpText);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }
}