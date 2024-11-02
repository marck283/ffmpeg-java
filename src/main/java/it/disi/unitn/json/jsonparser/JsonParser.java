package it.disi.unitn.json.jsonparser;

import com.google.gson.*;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.InvalidJSONFileException;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

/**
 * Wrapper class for Google's JSON parser.
 */
public class JsonParser {

    private final JsonObject obj;

    /**
     * This class's constructor. Builds an instance of this class using the given Reader.
     * @param reader The given Reader
     */
    public JsonParser(@NotNull Reader reader) {
        if(reader == null) {
            System.err.println((new InvalidArgumentException("The parameter passed to this constructor cannot be null.", "Il parametro " +
                    "fornito a questo costruttore non puo' essere null.")).getMessage());
            System.exit(7);
        }
        Gson gson = new GsonBuilder().create();
        obj = gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Returns an instance of the JsonArray class that represents the object identified by the name given as parameter.
     * @param arrName The name of the object to be returned
     * @return An instance of the object identified by the name given as parameter, or null should that object not exist
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     * @throws InvalidJSONFileException If the given JSON file does not contain any array identified by the name given
     * to this method
     */
    public JsonArray getJsonArray(@NotNull String arrName) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(obj, arrName);
        return jel.getAsJsonArray();
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
        JsonElement jel = checkField(obj, name);
        return jel.getAsString();
    }

    /**
     * Gets the String value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name if such element can be found, otherwise an empty string
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file does not contain any field identified by the given name
     */
    public String getString(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException,
            InvalidJSONFileException {
        JsonElement jel = checkField(getJsonObject(el), name);
        return jel.getAsString();
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
    private JsonElement getJsonElement(@NotNull JsonObject el, @NotNull String name) throws InvalidArgumentException {
        if(el == null) {
            throw new InvalidArgumentException("The JsonObject instance given to this method cannot be null.", "L'istanza " +
                    "di JsonObject fornita a questo metodo non puo' essere null.");
        }
        if(StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The parameters passed to this method cannot be null or empty strings.",
                    "I parametri passati a questo metodo non possono essere null o stringhe vuote.");
        }
        return el.get(name);
    }

    /**
     * Private method to check if a given field contains a valid value.
     * @param el The given JsonElement instance.
     * @param name The given field's name.
     * @return The JsonElement instance representing the requested field.
     * @throws InvalidArgumentException If the JsonObject instance given to this method is null or the given field's name
     * is null or an empty string
     * @throws InvalidJSONFileException If the JsonElement instance representing the requested field is null
     */
    private @NotNull JsonElement checkField(@NotNull JsonObject el, @NotNull String name) throws InvalidArgumentException,
            InvalidJSONFileException {
        JsonElement jel = getJsonElement(el, name);
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
     * @return The value of the element identified by the given name (if such element can be found)
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file does not contain any field identified by the given name
     */
    public float getFloat(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(getJsonObject(el), name);
        return jel.getAsFloat();
    }

    /**
     * Gets the integer value of the element identified by the given name.
     * @param el A JsonElement instance
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name if such element can be found
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file given as input to this library does not contain a field
     * identified by the name given to this method
     */
    public int getInt(@NotNull JsonElement el, @NotNull String name) throws InvalidArgumentException, InvalidJSONFileException {
        JsonElement jel = checkField(getJsonObject(el), name);
        return jel.getAsInt();
    }

    /**
     * Gets the integer value of the element identified by the given name from the JsonObject instance given to this
     * class's constructor upon its creation.
     * @param name The given element's name. This element has to be inside the structure of the given JsonElement
     * @return The value of the element identified by the given name if such element can be found
     * @throws InvalidArgumentException If any of the parameters passed to this method is null or an empty string
     * @throws InvalidJSONFileException If the JSON file given as input to this library does not contain a field
     * identified by the name given to this method
     */
    public int getInt(@NotNull String name) throws InvalidJSONFileException, InvalidArgumentException {
        JsonElement jel = checkField(obj, name);
        return jel.getAsInt();
    }
}
