
package com.lutu.golf;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.lutu.golf.ecrane.GolfScreen;

/**
 * Holds the logic that groups the {@link GolfScreen GolfScreens} in {@link Node Nodes}.
 */
public class ScreenGraph {

	private static final boolean DEBUG_NOTIFICATIONS = false;
	private final Set<Node> mNodes;
	private final Set<GolfScreen> mScreens;

	/**
	 * Constructs the {@link ScreenGraph} instance.
	 */
	public ScreenGraph() {
		mNodes = new HashSet<Node>();
		mScreens = new HashSet<GolfScreen>();
	}

	/**
	 * Adds a {@link GolfScreen} to the {@link ScreenGraph} root.
	 * 
	 * @param screen
	 *            {@link GolfScreen} to be added
	 */
	public void add(GolfScreen screen) {
		mScreens.add(screen);
	}

	/**
	 * Creates a {@link Node} that groups {@link GolfScreen GolfScreens}.
	 * 
	 * @return created {@link Node}
	 */
	public Node createNode() {
		final Node node = new Node(this);
		mNodes.add(node);
		return node;
	}

	/**
	 * Finds a {@link GolfScreen} instance based on the {@link ScreenType} provided.
	 * 
	 * @param screenType
	 *            {@link ScreenType} to look for in the {@link ScreenGraph}
	 * @return {@link GolfScreen} instance if found, null otherwise
	 */
	public GolfScreen find(ScreenType screenType) {
		for (GolfScreen screen : mScreens) {
			if (screen.getType().equals(screenType)) {
				return screen;
			}
		}

		return null;
	}

	/**
	 * {@link ScreenGraph} node that groups {@link GolfScreen GolfScreens}.
	 */
	public static final class Node {

		private final ScreenGraph mGraph;
		private final Set<GolfScreen> mScreens;

		private Node(ScreenGraph graph) {
			mGraph = graph;
			mScreens = new HashSet<GolfScreen>();
		}

		/**
		 * Adds a {@link GolfScreen} to the {@link Node}.
		 * 
		 * @param screen
		 *            {@link GolfScreen} to be added
		 * @return true if {@link GolfScreen} was added, false otherwise
		 */
		public boolean add(GolfScreen screen) {
			if (screen.getParentNode() != null) {
				// Already a part of the graph
				return false;
			}

			screen.setParentNode(this);
			return mScreens.add(screen);
		}

		/**
		 * Removes a {@link GolfScreen} from the {@link Node}.
		 * 
		 * @param screen
		 *            {@link GolfScreen} to be removed
		 * @return true if removed successfully, false otherwise
		 */
		public boolean remove(GolfScreen screen) {
			return mScreens.remove(screen);
		}

		/**
		 * Checks if the provided {@link GolfScreen} is in the {@link Node}.
		 * 
		 * @param screen
		 *            {@link GolfScreen} instance
		 * @return true if contained in the {@link Node}, false otherwise
		 */
		public boolean contains(GolfScreen screen) {
			return mScreens.contains(screen);
		}

		/**
		 * Searches for a {@link GolfScreen} based on the {@link ScreenType} provided.
		 * 
		 * @param screenType
		 *            {@link ScreenType} type
		 * @return {@link GolfScreen} instance or null if not found
		 */
		public GolfScreen find(ScreenType screenType) {
			for (GolfScreen screen : mScreens) {
				if (screenType.equals(screen.getType())) {
					return screen;
				}
			}

			for (Node node : mGraph.mNodes) {
				if (!equals(node)) {
					for (GolfScreen screen : node.mScreens) {
						if (screenType.equals(screen.getType())) {
							return screen;
						}
					}
				}
			}

			return mGraph.find(screenType);
		}

		/**
		 * Creates {@link GolfScreen GolfScreens}.
		 */
		public void createScreens() {
			for (GolfScreen screen : mScreens) {
				if (DEBUG_NOTIFICATIONS) {
					Gdx.app.debug(screen.getClass().getSimpleName(), "create");
				}
				screen.create();
			}
		}

		/**
		 * Disposes {@link GolfScreen GolfScreens}.
		 * 
		 * @param node
		 *            {@link Node} instance used to check if the {@link GolfScreen} should be disposed
		 */
		public void disposeScreens(Node node) {
			for (GolfScreen screen : mScreens) {
				if (!node.contains(screen)) {
					if (DEBUG_NOTIFICATIONS) {
						Gdx.app.debug(screen.getClass().getSimpleName(), "dispose");
					}
					screen.dispose();
				}
			}
		}
	}
}
