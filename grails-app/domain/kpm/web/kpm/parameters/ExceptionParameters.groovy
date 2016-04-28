package kpm.web.kpm.parameters

/**
 * Created by: Martin
 * Date: 07-08-14
 */
public class ExceptionParameters implements Serializable{
    public ExceptionParameters(){
        val = 0;
        val_step = 0;
        val_max = 0;
        use_range = false;
        isPercentage = false;
        createdDate = new Date();
    }

    int val
    int val_step
    int val_max
    boolean use_range
    boolean isPercentage
    Date createdDate

    KpmParameters owner
    static belongsTo = [owner: KpmParameters]

    static constraints = {
        val null: false, blank: false
        use_range null: false, blank: false
    }

    def updateValues(ExceptionParameters ep){
        this.lock();
        this.val = ep.val;
        this.val_step = ep.val_step;
        this.val_max = ep.val_max;
        this.use_range = ep.use_range;
        this.isPercentage = ep.isPercentage;
        this.createdDate = ep.createdDate;

        if (!this.save(flush: true, validate: true, failOnError: true)) {
            this.errors.each {
                println it
            }
        }
    }
}
