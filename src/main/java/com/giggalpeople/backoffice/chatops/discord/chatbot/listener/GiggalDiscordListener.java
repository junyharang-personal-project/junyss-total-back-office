package com.giggalpeople.backoffice.chatops.discord.chatbot.listener;

import com.giggalpeople.backoffice.chatops.discord.chatbot.command.CheckDiscordCommand;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.DiscordBotResponseMessageUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.API_RESPONSE_NOT_FOUND;

/**
 * <h2><b>디스 코드를 이용해서 메시지를 보낼 때 해당 메시지를 받고, 응답을 처리하는 Class</b></h2>
 */

@Slf4j
public class GiggalDiscordListener extends ListenerAdapter {

    /**
     * <b>디스코드 사용자 메시지를 받게 되면 처리하게 되는 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     */

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        EmbedBuilder discordEmbedMessage = new EmbedBuilder();

        if (user.isBot() || message.getContentDisplay().equals("") || !message.getContentDisplay().contains("기깔")) {
            return;
        }

        log.info(user.getName() + " 크루가 보낸 Discord Message : " + message.getContentDisplay());

        Map<String, List<String>> resultMap = CheckDiscordCommand.manufactureCommand(event, message.getContentDisplay());

        if (resultMap.isEmpty()) {
            throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
        }

        for (Map.Entry<String, List<String>> apiReturnMessage : resultMap.entrySet()) {
            if (apiReturnMessage.getKey().contains("내용 없음") || apiReturnMessage.getValue().contains(user.getName() + "님 명령어를 확인해 주세요.")) {
                discordEmbedMessage.setColor(Color.RED);
            }
        }
        DiscordBotResponseMessageUtil.createSendMessage(event, resultMap, discordEmbedMessage);
    }
}
