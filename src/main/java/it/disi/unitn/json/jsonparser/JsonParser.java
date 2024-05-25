package it.disi.unitn.json.jsonparser;

import com.google.gson.*;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.InvalidJSONFileException;
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
     * parametro, o null se tale parametro non esiste.
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     * @throws InvalidJSONFileException If the given JSON file does not contain any array identified by the name given
     * to this method
     */
    public JsonArray getJsonArray(@NotNull String arrName) throws InvalidArgumentException, InvalidJSONFileException {
        if(StringExt.checkNullOrEmpty(arrName)) {
            throw new InvalidArgumentException("The parameter given to this method cannot be null or an empty string.",
                    "Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
        }

        JsonArray jsonArr = obj.getAsJsonArray(arrName);
        if(jsonArr == null) {
            throw new InvalidJSONFileException("The given JSON file must contain at least one array of JSON objects with " +
                    "the name given to this method.", "Il file JSON fornito deve contenere almeno un array di oggetti " +
                    "JSON con il nome fornito a questo metodo.");
        }
        return obj.getAsJsonArray(arrName);
    }

    /**
     * This method returns tne value of the attribute identified by the given parameter.
     * @param name The name of the parameter whose value must be returned. This parameter cannot be null or an empty
     *             string.
     * @return The value of the attribute identified by the given parameter (if such an attribute exists).
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file does not contain any JSON element with the given name
     */
    public String getString(@NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        if(StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The parameter given to this method cannot be null or an empty string.",
                    "Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
        }

        JsonElement jel = obj.get(name);
        if(jel == null) {
            throw new InvalidJSONFileException("The field \"" + name + "\" is missing.", "Il campo \"" + name + "\" non " +
                    "e' presente.");
        }
        //if(jel != null) {
            return jel.getAsString();
        //}
        //return "";
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
        return getJsonObject(el).get(name);
    }

    /**
     * Gets the String value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name if such element can be found, otherwise an empty string
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file does not contain any field identified by the given name
     */
    public String getString(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(el, name);
        //if(jel != null) {
        return jel.getAsString();
        //}
        //return "";
    }

    /**
     * Private method to check if a given field contains a valid value.
     * @param el The given JsonElement instance.
     * @param name The given field's name.
     * @return The JsonElement instance representing the requested field.
     * @throws InvalidArgumentException If the given field's name is null or an empty string
     * @throws InvalidJSONFileException If the JsonElement instance representing the requested field is null
     */
    private @NotNull JsonElement checkField(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException,
            InvalidJSONFileException {
        if(el == null || StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The parameters passed to this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }

        JsonElement jel = getElement(el, name);
        if(jel == null) {
            throw new InvalidJSONFileException("The field \"" + name + "\" is missing.", "Il campo \"" + name + "\" non " +
                    "e' presente.");
        }

        return jel;
    }

    /**
     * Gets the floating point value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name (if such element can be found), otherwise -1F
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file does not contain any field identified by the given name
     */
    public float getFloat(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(el, name);
        //if(jel != null) {
        return jel.getAsFloat();
        //}
        //return "";
    }

    /**
     * Gets the integer value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name if such element can be found, otherwise -1
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file given as input to this library does not contain a field
     * identified by the name given to this method
     */
    public int getInt(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(el, name);
        //if(jel != null) {
        return jel.getAsInt();
        //}
        //return "";
    }
}
