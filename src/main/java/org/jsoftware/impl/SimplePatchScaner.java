package org.jsoftware.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.jsoftware.config.Patch;
import org.jsoftware.config.PatchScaner;
import org.jsoftware.impl.commons.FilenameUtils;
import org.jsoftware.log.Log;
import org.jsoftware.log.LogFactory;

public abstract class SimplePatchScaner implements PatchScaner {
	private Log log = LogFactory.getInstance();
	
	public List<Patch> scan(File baseDir, String[] paths) throws DuplicatePatchNameException {	
		List<DirMask> dirMasks = parsePatchDirs(baseDir, paths);
		LinkedList<Patch> list = new LinkedList<Patch>();
		for(DirMask dm : dirMasks) {
			log.debug("Scan " + dm.getDir().getAbsolutePath() + " for " + dm.getMask());
			LinkedList<Patch> dirList = new LinkedList<Patch>();
			File[] flist = dm.getDir().listFiles(new WildchardMaskFileFilter(dm.getMask()));
			for (File f : flist) {
				Patch p = new Patch();
				p.setFile(f);
				p.setName(f.getName().substring(0, f.getName().length() - 4));
				dirList.add(p);
			}
			sortDirectory(dirList);
			for(Patch patch1 : dirList) {
				for(Patch patch2 : list) {
					if (patch1.getName().equals(patch2.getName())) {
						throw new DuplicatePatchNameException(this, patch1, patch2);
					}
				}
				list.add(patch1);
			}
		}
		sortAll(list);
		return list;
	}

	
	protected abstract void sortDirectory(List<Patch> dirPatchList);
	
	protected abstract void sortAll(List<Patch> allPatchList);


	private List<DirMask> parsePatchDirs(File basePath, String[] dirs) {
		List<DirMask> dirMasks = new LinkedList<DirMask>();
		for(String s : dirs) {
			s = s.trim();
			if (s.length() == 0) {
				log.debug(" - entry skipped");
				continue;
			}
			File f = new File(s);
			if (! f.isAbsolute()) {
				f = new File(basePath, s);
			}
			if (f.isDirectory()) {
				DirMask dm = new DirMask(f);
				dirMasks.add(dm);
				log.debug(" + entry directory " + dm);
			} else {
				String wch = f.getName();
				DirMask dm = new DirMask(f.getParentFile());
				dm.setMask(wch);
				log.debug(" + entry single file " + dm);
			}
		}
		for(DirMask dm : dirMasks) {
			dm.validate();
		}
		return dirMasks;
	}
	
		
	class WildchardMaskFileFilter implements FileFilter {
		private String mask;
		public WildchardMaskFileFilter(String mask) {
			this.mask = mask;
		}

		public boolean accept(File pathname) {
			String fn = pathname.getName();
			boolean b = FilenameUtils.wildcardMatchOnSystem(fn, mask);
			log.debug("Check WildchardMaskFileFilter - " + fn + " is " + b);
			return b;
		}
	}

}

class DirMask {
	private File dir;
	private String mask = "*.sql";
	
	public DirMask(File dir) {
		this.dir = dir;
	}
	
	public void validate() throws IllegalArgumentException {
		if (! dir.exists()) {
			throw new IllegalArgumentException("Directory " + dir.getPath() + " does not exist.");
		}
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
	public File getDir() {
		return dir;
	}
	
	public String getMask() {
		return mask;
	}
	
	@Override
	public String toString() {
		return "(" + dir.getAbsolutePath() + ":" + getMask() + ")";
	}
	
}
