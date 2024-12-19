package ar.edu.unq.epersgeist.helper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DatabaseCleanerService {


    @Autowired
    private DataDao dataDao;

    public void deleteAll(){
        dataDao.deleteALL();
    }

}
