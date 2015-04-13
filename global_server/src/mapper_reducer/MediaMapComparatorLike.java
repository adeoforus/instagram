package mapper_reducer;

import java.util.Comparator;

public class MediaMapComparatorLike implements Comparator<MediaMap> {

	@Override
	public int compare(MediaMap m1, MediaMap m2) {
		int i = 0;
		if (m1.getLikes_count() > m2.getLikes_count()) {
			i = -1;
		}
		if (m1.getLikes_count() < m2.getLikes_count()) {
			i = 1;
		}
		if (m1.getLikes_count() == m2.getLikes_count()) {
			i = 0;
		}

		return i;
	}

}
