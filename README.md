# ChatGpt-Java

#### ChatGpt Java版本封装。

#### 支持sse、websocket。实现流式传输数据。

## 官方API参考

- [Models](https://platform.openai.com/docs/api-reference/models)
- [Completions](https://platform.openai.com/docs/api-reference/completions)
- [Chat Completions](https://platform.openai.com/docs/api-reference/chat/create)
- [Edits](https://platform.openai.com/docs/api-reference/edits)
- [Embeddings](https://platform.openai.com/docs/api-reference/embeddings)
- [Files](https://platform.openai.com/docs/api-reference/files)
- [Fine-tunes](https://platform.openai.com/docs/api-reference/fine-tunes)
- [Images](https://platform.openai.com/docs/api-reference/images)
- [Moderations](https://platform.openai.com/docs/api-reference/moderations)

### 导入Maven依赖包

```xml

<dependency>
    <groupId>com.theokanning.openai-gpt3-java</groupId>
    <artifactId>{api|client|service}</artifactId>
    <version>version</version>
</dependency>
```

### 如何配置

- 修改 application.properties 的token属性,填写自己的token
