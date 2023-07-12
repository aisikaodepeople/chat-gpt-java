package com.chatgpt.demo.contants;

/**
 * ChatGPT模型
 */
public enum ChatGtpModelEnum {

    gpt4("gpt-4"),
    gpt40314("gpt-4-0314"),
    gpt40613("gpt-4-0613"),
    gpt432k("gpt-4-32k"),
    gpt432k0314("gpt-4-32k-0314"),
    gpt432k0613("gpt-4-32k-0613"),
    gpt35turbo("gpt-3.5-turbo"),
    gpt35turbo16k("gpt-3.5-turbo-16k"),
    gpt35turbo0301("gpt-3.5-turbo-0301"),
    gpt35turbo0613("gpt-3.5-turbo-0613"),
    gpt35turbo16k0613("gpt-3.5-turbo-16k-0613");

    private String model;

    ChatGtpModelEnum(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

}
