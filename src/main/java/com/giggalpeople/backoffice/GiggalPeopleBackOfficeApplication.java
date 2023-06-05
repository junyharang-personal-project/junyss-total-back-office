package com.giggalpeople.backoffice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import com.giggalpeople.backoffice.chatops.discord.chatbot.common.message.DiscordBotResponseMessage;
import com.giggalpeople.backoffice.chatops.discord.chatbot.listener.GiggalDiscordListener;

@SpringBootApplication
public class GiggalPeopleBackOfficeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GiggalPeopleBackOfficeApplication.class, args);
		DiscordBotToken discordBotTokenEntity = context.getBean(DiscordBotToken.class);
		String discordBotToken = discordBotTokenEntity.getDiscordBotToken();

		JDABuilder.createDefault(discordBotToken)
			.setActivity(Activity.playing(DiscordBotResponseMessage.discordBotStatusRandomMessage()))
			.enableIntents(GatewayIntent.MESSAGE_CONTENT)
			.addEventListeners(new GiggalDiscordListener())
			.build();
	}
}

/**
 * <h2><b>Discord Bot Token을 Application.yml에 저장하고, 가져오기 위한 Class</b></h2>
 */

@Component
class DiscordBotToken {

	/**
	 * <b>Application.yml에 저장된 Discord Bot token</b>
	 */
	@Value("${discord.bot.token}")
	private String discordBotToken;

	/**
	 * <b>Discord Bot Token을 저장하고 있는 변수에 값을 가져오기 위한 Getter</b>
	 * @return Discord Bot Token
	 */
	public String getDiscordBotToken() {
		return discordBotToken;
	}
}
