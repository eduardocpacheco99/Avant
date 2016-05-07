import java.util.ArrayList;
import java.util.List;


public abstract class ExtratorWebsite {
	protected List<ExtratorWebsiteStep> steps = new ArrayList<ExtratorWebsiteStep>();

	public List<ExtratorWebsiteStep> getSteps() {
		return steps;
	}
}
