package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import util.Managers;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setManager() {
        manager = (InMemoryTaskManager) Managers.getDefault();
    }

}