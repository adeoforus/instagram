package mapper_reducer;

import java.util.Comparator;

public class MediaMapComparatorCreatedTime implements Comparator<MediaMap> {

	@Override
	public int compare(MediaMap m1, MediaMap m2) {
		int i = 0;

		String s1 = m1.getCreated_time();
		String s2 = m2.getCreated_time();
		int i1 = Integer.parseInt(s1);
		int i2 = Integer.parseInt(s2);

		if (i1 > i2) {
			i = -1;
		}
		if (i1 < i2) {
			i = 1;
		}
		if (i1 == i2) {
			i = 0;
		}

		return i;
	}

}