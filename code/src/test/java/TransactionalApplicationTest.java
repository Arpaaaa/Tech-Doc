import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tech.doc.spring.service.TransactionalService;

/**
 * @author ArpatNurmamat <airepatinuermaimaiti@kuaishou.com>
 * Created on 2025-04-13
 */
@SpringBootTest(classes = TransactionalService.class)
public class TransactionalApplicationTest {

    @Autowired
    private TransactionalService transactionalService;
    @Test
    public void TransactionalServiceTest() {
        transactionalService.save("testing");
    }
}