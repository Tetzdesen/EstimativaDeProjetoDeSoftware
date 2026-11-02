package com.github.tetzdesen.estimativalog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Adapter que usa o Gson para manter o arquivo como um array JSON válido,
 * com um objeto por registro.
 */
public class JsonLoggerAdapter implements Logger {

    private final Path arquivo;
    private final Gson gson;

    public JsonLoggerAdapter(Path arquivo) {
        this.arquivo = arquivo;
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    @Override
    public synchronized void registrar(LogRegistro registro) {
        try {
            Path pai = arquivo.toAbsolutePath().getParent();
            if (pai != null) {
                Files.createDirectories(pai);
            }
            JsonArray registros = lerExistentes();
            registros.add(paraJson(registro));
            Files.writeString(arquivo, gson.toJson(registros), StandardCharsets.UTF_8);
        } catch (IOException | JsonSyntaxException | IllegalStateException e) {
            throw new LogException("Não foi possível gravar o log JSON em " + arquivo, e);
        }
    }

    private JsonArray lerExistentes() throws IOException {
        if (Files.notExists(arquivo)) {
            return new JsonArray();
        }
        String conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);
        if (conteudo.isBlank()) {
            return new JsonArray();
        }
        return JsonParser.parseString(conteudo).getAsJsonArray();
    }

    private static JsonObject paraJson(LogRegistro r) {
        JsonObject obj = new JsonObject();
        obj.addProperty("data", r.getData());
        obj.addProperty("hora", r.getHora());
        obj.addProperty("usuario", r.getUsuario());
        obj.addProperty("operacao", r.getOperacao());
        obj.addProperty("projeto", r.getNomeProjeto());
        obj.addProperty("status", r.isFalha() ? "FALHA" : "SUCESSO");
        if (r.isFalha()) {
            obj.addProperty("falha", r.getMensagemFalha());
        }
        obj.addProperty("mensagem", r.getMensagem());
        return obj;
    }

    @Override
    public FormatoLog getFormato() {
        return FormatoLog.JSON;
    }

    public Path getArquivo() {
        return arquivo;
    }
}
