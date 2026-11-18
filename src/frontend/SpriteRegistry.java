package frontend;

import java.io.IOException;
import java.util.HashMap;

public class SpriteRegistry {
    private final HashMap<String,Sprite> sprites = new HashMap<>();

    public void register(String spriteId, String imageName, int width, int height) throws IOException {
        //right now it is assumed that every spriteId is unique however I will want to later hashCode this
        sprites.put(spriteId,new Sprite(imageName,width,height));//slot the new item in
    }

    public Sprite access(String spriteId){
        if(!sprites.containsKey(spriteId)){
            //There is an issue with inconsistent data from the backend, just give back null, we also could throw
            return null;
        }else{
            return sprites.get(spriteId);
        }
    }

}
