package com.giggalpeople.backoffice.chatops.logback.appender.discord.config;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CREATE_DISCORD_APPEND_MESSAGE_FAILURE;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.APICallException;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.Author;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.EmbedObject;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.Field;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.Footer;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.Image;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.JsonObject;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.Thumbnail;
import com.giggalpeople.backoffice.chatops.logback.appender.exception.ErrorLogAppenderException;
import com.giggalpeople.backoffice.common.util.ApiCallUtil;

import lombok.Setter;

/**
 * <h2><b>Discord Webhook 실행을 위한 Class</b></h2>
 */
@Setter
public class DiscordWebHook {

	private final String urlString;
	private final List<EmbedObject> embeds = new ArrayList<>();
	private String content;
	private String username;
	private String avatarUrl;
	private boolean tts;

	/**
	 * <b>새로운 Discord Webhook 인스턴스를 구성합니다.</b>
	 *
	 * @param urlString DiscordAppender 객체에서 application.yml에 등록된 Discord WebHook 주소를 가져와 전달해 준 Discrod WebHook 주소
	 */
	public DiscordWebHook(String urlString) {
		this.urlString = urlString;
	}

	/**
	 * <b>Discord에 Log Back Message를 보낼 때, Embed 형식으로 보내주기 위한 Method</b>
	 *
	 * @param embed Discord에 Log Back Message를 보낼 때, Embed 형식 객체
	 */
	public void addEmbed(EmbedObject embed) {
		this.embeds.add(embed);
	}

	/**
	 * <b>Discrod로 Log Back Message를 실제로 보내기 위한 Method</b>
	 */
	public void execute() {
		if (this.content == null && this.embeds.isEmpty()) {
			throw new IllegalArgumentException("컨텐츠를 설정하거나 하나 이상의 embedobject를 추가해야 합니다.");
		}

		try {
			ApiCallUtil.callDiscordAppenderPostAPI(
				this.urlString, createDiscordEmbedObject(
					this.embeds, initializerDiscordSendForJsonObject(new JsonObject())
				));

		} catch (IOException ioException) {
			throw new APICallException(ioException.getMessage(), ioException);
		}
	}

	private JsonObject initializerDiscordSendForJsonObject(JsonObject json) {
		json.put("content", this.content);
		json.put("username", this.username);
		json.put("avatar_url", this.avatarUrl);
		json.put("tts", this.tts);
		return json;
	}

	private JsonObject createDiscordEmbedObject(List<EmbedObject> embeds, JsonObject json) {
		if (embeds.isEmpty()) {
			throw new ErrorLogAppenderException(CREATE_DISCORD_APPEND_MESSAGE_FAILURE,
				CREATE_DISCORD_APPEND_MESSAGE_FAILURE.getMessage());
		}

		List<JsonObject> embedObjects = new ArrayList<>();

		for (EmbedObject embed : embeds) {
			JsonObject jsonEmbed = new JsonObject();

			jsonEmbed.put("title", embed.getTitle());
			jsonEmbed.put("description", embed.getDescription());
			jsonEmbed.put("url", embed.getUrl());

			processDiscordEmbedColor(embed, jsonEmbed);
			processDiscordEmbedFooter(embed.getFooter(), jsonEmbed);
			processDiscordEmbedImage(embed.getImage(), jsonEmbed);
			processDiscordEmbedThumbnail(embed.getThumbnail(), jsonEmbed);
			processDiscordEmbedAuthor(embed.getAuthor(), jsonEmbed);
			processDiscordEmbedMessageFields(embed.getFields(), jsonEmbed);

			embedObjects.add(jsonEmbed);
		}
		json.put("embeds", embedObjects.toArray());

		return json;
	}

	private void processDiscordEmbedColor(EmbedObject embed, JsonObject jsonEmbed) {
		if (embed.getColor() != null) {
			Color color = embed.getColor();
			int rgb = color.getRed();
			rgb = (rgb << 8) + color.getGreen();
			rgb = (rgb << 8) + color.getBlue();

			jsonEmbed.put("color", rgb);
		}
	}

	private void processDiscordEmbedFooter(Footer footer, JsonObject jsonEmbed) {
		if (footer != null) {
			JsonObject jsonFooter = new JsonObject();
			jsonFooter.put("text", footer.getText());
			jsonFooter.put("icon_url", footer.getIconUrl());
			jsonEmbed.put("footer", jsonFooter);
		}
	}

	private void processDiscordEmbedImage(Image image, JsonObject jsonEmbed) {
		if (image != null) {
			JsonObject jsonImage = new JsonObject();
			jsonImage.put("url", image.getUrl());
			jsonEmbed.put("image", jsonImage);
		}
	}

	private void processDiscordEmbedThumbnail(Thumbnail thumbnail, JsonObject jsonEmbed) {
		if (thumbnail != null) {
			JsonObject jsonThumbnail = new JsonObject();
			jsonThumbnail.put("url", thumbnail.getUrl());
			jsonEmbed.put("thumbnail", jsonThumbnail);
		}
	}

	private void processDiscordEmbedAuthor(Author author, JsonObject jsonEmbed) {
		if (author != null) {
			JsonObject jsonAuthor = new JsonObject();
			jsonAuthor.put("name", author.getName());
			jsonAuthor.put("url", author.getUrl());
			jsonAuthor.put("icon_url", author.getIconUrl());
			jsonEmbed.put("author", jsonAuthor);
		}
	}

	private void processDiscordEmbedMessageFields(List<Field> fields, JsonObject jsonEmbed) {
		List<JsonObject> jsonFields = new ArrayList<>();

		for (Field field : fields) {
			JsonObject jsonField = new JsonObject();

			jsonField.put("name", field.getName());
			jsonField.put("value", field.getValue());
			jsonField.put("inline", field.isInline());

			jsonFields.add(jsonField);
		}

		jsonEmbed.put("fields", jsonFields.toArray());
	}
}