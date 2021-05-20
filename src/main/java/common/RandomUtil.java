package common;

import java.util.Random;

public class RandomUtil {
    private Random random;
    private RandomUtil(){
        random = new Random();
    }
    public int getRandom(int size){
        return random.nextInt(size);
    }
    private static class IDHolder{
        private static RandomUtil INSTANCE = new RandomUtil();
    }
    public static RandomUtil getInstance(){
        return IDHolder.INSTANCE;
    }
}
