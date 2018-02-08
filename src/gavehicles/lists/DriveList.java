package gavehicles.lists;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.vehicles.GeneralDriveOutput;
import java.util.ArrayList;

public class DriveList extends ArrayList<AbstractDriveOutput> {

    public AbstractDriveOutput avgDrives() {
        double left = 0;
        double right = 0;

        for (AbstractDriveOutput next : this) {
            left += next.getLeftWheelOutput();
            right += next.getRightWheelOutput();
        }

        left = left / this.size();
        right = right / this.size();

        return new GeneralDriveOutput(left, right);
    }

    public AbstractDriveOutput sum() {
        double left = 0;
        double right = 0;

        for (AbstractDriveOutput next : this) {
            left += next.getLeftWheelOutput();
            right += next.getRightWheelOutput();
        }
        
        return new GeneralDriveOutput(left, right);
    }

}
