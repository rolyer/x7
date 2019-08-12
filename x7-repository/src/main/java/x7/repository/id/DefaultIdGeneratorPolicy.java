package x7.repository.id;

import org.springframework.data.redis.core.StringRedisTemplate;
import x7.repository.IdGenerator;

import java.util.List;

public class DefaultIdGeneratorPolicy implements IdGeneratorPolicy {


    private StringRedisTemplate stringRedisTemplate;
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public long createId(String clzName) {

        return this.stringRedisTemplate.opsForHash().increment(ID_MAP_KEY,clzName,1);

    }

    @Override
    public void onStart(List<IdGenerator> idGeneratorList) {

            System.out.println("\n" + "----------------------------------------");

            for (IdGenerator generator : idGeneratorList) {
                String name = generator.getClzName();
                long maxId = generator.getMaxId();

                String idInRedis = null;
                Object obj = this.stringRedisTemplate.opsForHash().get(IdGeneratorPolicy.ID_MAP_KEY, name);

                if (obj != null) {
                    idInRedis =  obj.toString().trim();
                }

                System.out.println(name + ",test, idInDB = " + maxId + ", idInRedis = " + idInRedis);


                if (idInRedis == null) {
                    this.stringRedisTemplate.opsForHash().put(IdGeneratorPolicy.ID_MAP_KEY, name, String.valueOf(maxId));
                } else if (idInRedis != null && maxId > Long.valueOf(idInRedis)) {
                    this.stringRedisTemplate.opsForHash().put(IdGeneratorPolicy.ID_MAP_KEY, name, String.valueOf(maxId));
                }

                System.out.println(name + ",final, idInRedis = " + this.stringRedisTemplate.opsForHash().get(IdGeneratorPolicy.ID_MAP_KEY, name));


            }
            System.out.println("----------------------------------------" + "\n");
    }
}
