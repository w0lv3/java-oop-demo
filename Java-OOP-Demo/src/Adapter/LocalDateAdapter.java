package Adapter;

import Common.Commons;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Adapter to handle the JSON string value to LocalDate
public class LocalDateAdapter implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), Commons.dateFormatter);
    }
}
