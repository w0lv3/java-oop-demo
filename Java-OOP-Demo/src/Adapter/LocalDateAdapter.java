package Adapter;

import Common.Commons;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

//Adapter to handle the JSON string value to LocalDate for both serialize and serialize
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Override //Custom Serializer
    public JsonElement serialize(LocalDate sourceDate, Type type, JsonSerializationContext context){
        return new JsonPrimitive(sourceDate.format(Commons.dateFormatter));
    }

    @Override //Custom Deserializer
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), Commons.dateFormatter);
    }


}
