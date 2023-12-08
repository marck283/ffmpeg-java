package it.disi.unitn.jsonparser;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

public class JsonParser {
    private final JsonObject obj;

    public JsonParser(@NotNull Reader reader) {
        Gson gson = new GsonBuilder().create();
        obj = gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Ritorna un'istanza della classe JsonArray che rappresenta l'oggetto identificato dal nome fornito
     * come parametro.
     * @param arrName Il nome dell'oggetto da ritornare
     * @return Un'istanza di JsonArray che rappresenta l'oggetto identificato dal nome fornito come
     * parametro.
     */
    public JsonArray getJsonArray(@NotNull String arrName) {
        if(arrName == null || arrName.isEmpty()) {
            System.err.println("Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
            System.exit(1);
        }
        return obj.getAsJsonArray(arrName);
    }

    /**
     * Ritorna una stringa avente come valore quello dell&rsquo;attributo identificato dal parametro fornito.
     * @param name Il nome del parametro di cui ritornare il valore. Non pu&ograve; essere null o una
     *             stringa vuota.
     * @return Una stringa avente come valore quello dell&rsquo;attributo identificato dal parametro fornito.
     */
    public String getString(@NotNull String name) {
        if(name == null || name.isEmpty()) {
            System.err.println("Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
            System.exit(1);
        }
        return obj.get(name).getAsString();
    }

    /**
     * Gets a new JsonObject instance from the given JsonElement
     * @param e The given JsonElement
     * @return A new JsonObject instance from the given JsonElement
     */
    public JsonObject getJsonObject(@NotNull JsonElement e) {
        return e.getAsJsonObject();
    }

    /**
     * Gets the element identified by the given name inside the given JsonElement.
     * @param el The given JsonElement
     * @param name The given element name
     * @return A JsonElement instance representing the so found element
     */
    private JsonElement getElement(@NotNull JsonElement el, @NotNull String name) {
        if(el == null || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("I parametri passati a questo metodo non possono essere null o" +
                    " stringhe vuote.");
        }
        return getJsonObject(el).get(name);
    }

    /**
     * Gets the String value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     */
    public String getString(@NotNull JsonElement el, @NotNull String name) {
        if(el == null || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("I parametri passati a questo metodo non possono essere null o" +
                    " stringhe vuote.");
        }
        return getElement(el, name).getAsString();
    }

    /**
     * Gets the floating point value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     */
    public float getFloat(@NotNull JsonElement el, @NotNull String name) {
        if(el == null || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("I parametri passati a questo metodo non possono essere null o" +
                    " stringhe vuote.");
        }
        return getElement(el, name).getAsFloat();
    }

    /**
     * Gets the integer value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     */
    public int getInt(@NotNull JsonElement el, @NotNull String name) {
        if(el == null || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("I parametri passati a questo metodo non possono essere null o" +
                    " stringhe vuote.");
        }
        return getElement(el, name).getAsInt();
    }
}
