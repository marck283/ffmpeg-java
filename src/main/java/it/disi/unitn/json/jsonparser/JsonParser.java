package it.disi.unitn.json.jsonparser;

import com.google.gson.*;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

/**
 * Classe wrapper per il parser JSON di Google.
 */
public class JsonParser {
    private final JsonObject obj;

    /**
     * Il costruttore della classe. Costruisce un'istanza di questa classe utilizzando un Reader.
     * @param reader Il Reader utilizzato
     * @throws InvalidArgumentException If the parameter passed to this constructor is null
     */
    public JsonParser(@NotNull Reader reader) throws InvalidArgumentException {
        if(reader == null) {
            throw new InvalidArgumentException("The parameter passed to this constructor cannot be null.", "Il parametro " +
                    "fornito a questo costruttore non puo' essere null.");
        }
        Gson gson = new GsonBuilder().create();
        obj = gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Ritorna un'istanza della classe JsonArray che rappresenta l'oggetto identificato dal nome fornito
     * come parametro.
     * @param arrName Il nome dell'oggetto da ritornare
     * @return Un'istanza di JsonArray che rappresenta l'oggetto identificato dal nome fornito come
     * parametro.
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     */
    public JsonArray getJsonArray(@NotNull String arrName) throws InvalidArgumentException {
        if(arrName == null || arrName.isEmpty()) {
            throw new InvalidArgumentException("The parameter given to this method cannot be null or an empty string.",
                    "Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
        }
        return obj.getAsJsonArray(arrName);
    }

    /**
     * Ritorna una stringa avente come valore quello dell&rsquo;attributo identificato dal parametro fornito.
     * @param name Il nome del parametro di cui ritornare il valore. Non pu&ograve; essere null o una
     *             stringa vuota.
     * @return Una stringa avente come valore quello dell&rsquo;attributo identificato dal parametro fornito.
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     */
    public String getString(@NotNull String name) throws InvalidArgumentException {
        if(name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The parameter given to this method cannot be null or an empty string.",
                    "Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
        }
        return obj.get(name).getAsString();
    }

    /**
     * Gets a new JsonObject instance from the given JsonElement
     * @param e The given JsonElement
     * @return A new JsonObject instance from the given JsonElement
     * @throws InvalidArgumentException If the parameter declared for this method is null
     */
    public JsonObject getJsonObject(@NotNull JsonElement e) throws InvalidArgumentException {
        if(e == null) {
            throw new InvalidArgumentException("The parameter declared for this method cannot be null.", "Il parametro " +
                    "fornito a questo metodo non puo' essere null.");
        }
        return e.getAsJsonObject();
    }

    /**
     * Gets the element identified by the given name inside the given JsonElement.
     * @param el The given JsonElement
     * @param name The given element name
     * @return A JsonElement instance representing the so found element
     * @throws InvalidArgumentException If any of the parameters declared for this method is null or an empty string
     */
    private JsonElement getElement(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException {
        if(el == null || name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The parameters declared for this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }
        return getJsonObject(el).get(name);
    }

    /**
     * Gets the String value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     */
    public String getString(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException {
        if(el == null || name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The parameters passed to this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }
        return getElement(el, name).getAsString();
    }

    /**
     * Gets the floating point value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     */
    public float getFloat(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException {
        if(el == null || name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The parameters passed to this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }
        return getElement(el, name).getAsFloat();
    }

    /**
     * Gets the integer value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     */
    public int getInt(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException {
        if(el == null || name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The parameters passed to this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }
        return getElement(el, name).getAsInt();
    }
}
