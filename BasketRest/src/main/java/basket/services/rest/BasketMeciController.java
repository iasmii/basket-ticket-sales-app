package basket.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.model.Meci;
import ro.persistance.IRepoMeci;
import ro.persistance.repository.RepoMeci;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/basket/meciuri")
public class BasketMeciController {
    @Autowired
    private IRepoMeci repoMeci;

    @RequestMapping(method = RequestMethod.GET)
    public Meci[] getAll(){
        System.out.println("Getting all meciuri");
        Iterable<Meci> meciuriIterable = repoMeci.findAll();
        List<Meci> meciuriList = new ArrayList<>();

        for (Meci meci : meciuriIterable) {
            meciuriList.add(meci);
        }

        Meci[] meciuriArray = meciuriList.toArray(new Meci[0]);
        return meciuriArray;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable int id){
        System.out.println("Getting by id: "+id);
        Meci meci=repoMeci.findOne(id);
        if (meci==null)
            return new ResponseEntity<String>("Meci not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Meci>(meci,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Meci create(@RequestBody Meci meci){
        System.out.println("Saving meci");
        Meci meciCuId=repoMeci.save(meci);
        System.out.println("rest controller: "+meciCuId);
        return meciCuId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Meci update(@RequestBody Meci meci){
        System.out.println("Updating meci");
        repoMeci.update(meci);
        return meci;
    }

    @RequestMapping(value="{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id){
        System.out.println("Deleting meci");
        try{
            repoMeci.delete(id);
            return new ResponseEntity<Meci>(HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println("Delete exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String meciError(Exception e){
        return e.getMessage();
    }
}
