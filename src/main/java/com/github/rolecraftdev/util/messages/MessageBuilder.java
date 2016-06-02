package com.github.rolecraftdev.util.messages;

public class MessageBuilder {
    private ColourScheme scheme;
    private StringBuilder message;

    public MessageBuilder() {
        this(ColourScheme.DEFAULT);
    }

    public MessageBuilder(final ColourScheme scheme) {
        this.scheme = scheme;
    }

    public ColourScheme scheme() {
        return scheme;
    }

    public MessageBuilder scheme(ColourScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public MessageBuilder highlight() {
        return append(scheme.getHighlight().toString());
    }

    public MessageBuilder light() {
        return append(scheme.getLight().toString());
    }

    public MessageBuilder dark() {
        return append(scheme.getDark().toString());
    }

    public MessageBuilder msg() {
        return append(scheme.getMsg().toString());
    }

    public MessageBuilder prefix() {
        return append(scheme.getPrefix().toString());
    }

    public MessageBuilder append(String string) {
        return doAppend(scheme.format(string));
    }

    public FormattedMessage toMessage() {
        return new FormattedMessage(message.toString());
    }

    private MessageBuilder doAppend(String string) {
        message.append(string);
        return this;
    }
}
