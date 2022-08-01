package hamburg.kaischmidt.sunshine.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataDomainService {
    @Getter
    private static DataDomainService domainService = new DataDomainService();

    private Queue<Data> queue = new ConcurrentLinkedQueue<>();

    public void data(Data data) {
        data.setCreated(LocalDateTime.now());
        queue.add(data);
    }

    public boolean isThereDataToProcess() {
        return !queue.isEmpty();
    }

    public Data getData() {
        return queue.remove();
    }

}
