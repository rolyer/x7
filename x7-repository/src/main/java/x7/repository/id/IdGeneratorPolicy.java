package x7.repository.id;

import x7.repository.IdGenerator;

import java.util.List;

public interface IdGeneratorPolicy {

    String ID_MAP_KEY = "ID_MAP_KEY";

    long createId(String clzName);

    void onStart(List<IdGenerator> idGeneratorList);
}
