package br.com.pos.unicamp.vrep;

import java.util.ArrayList;
import java.util.List;

import br.com.pos.unicamp.vrep.behaviors.PathPlanner;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Node;
import br.com.pos.unicamp.vrep.charts.BaseChart;
import br.com.pos.unicamp.vrep.charts.GroundTruthChart;
import br.com.pos.unicamp.vrep.charts.ObstacleChart;
import br.com.pos.unicamp.vrep.charts.OdometerChart;
import br.com.pos.unicamp.vrep.robots.Pioneer;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import org.opencv.core.Core;

/**
 * Main class to run app
 *
 */
public class App {

	static {
		loadOpenCVLibrary();
	}

	/**
	 * Static goal path's to GoToGoal
	 * @param args
	 */
	public static void main(final String[] args) {

		RemoteAPI.session(() -> {
			final Pioneer pioneer = new Pioneer();

			// You can remove the comment bellow to see some charts!
			// (it's commented to increase the performance... shhh)
			// chartsFor(pioneer).forEach(BaseChart::start);

			final PathPlanner pathPlanner2 = new PathPlanner(pioneer, 39, 33);
			final List<Node> path2 = pathPlanner2.getPath();
			pioneer.start(path2);

			final PathPlanner pathPlanner3 = new PathPlanner(pioneer, 23, 46);
			final List<Node> path3 = pathPlanner3.getPath();
			pioneer.start(path3);

			final PathPlanner pathPlanner4 = new PathPlanner(pioneer, 21, 30);
			final List<Node> path4 = pathPlanner4.getPath();
			pioneer.start(path4);

			final PathPlanner pathPlanner5 = new PathPlanner(pioneer, 14, 28);
			final List<Node> path5 = pathPlanner5.getPath();
			pioneer.start(path5);

			final PathPlanner pathPlanner10 = new PathPlanner(pioneer, 12, 21);
			final List<Node> path10 = pathPlanner10.getPath();
			pioneer.start(path10);

			final PathPlanner pathPlanner6 = new PathPlanner(pioneer, 19, 19);
			final List<Node> path6 = pathPlanner6.getPath();
			pioneer.start(path6);

			final PathPlanner finalPathPlanner = new PathPlanner(pioneer, 38, 6);
			final List<Node> finalPath = finalPathPlanner.getPath();

			pioneer.start(finalPath);

		});
	}

	private static ArrayList<BaseChart> chartsFor(final Pioneer pioneer) {
		return new ArrayList<BaseChart>() {
			{
				add(new GroundTruthChart(pioneer));
				add(new OdometerChart(pioneer));
				add(new ObstacleChart(pioneer));
			}
		};
	}

	private static void loadOpenCVLibrary() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
}
