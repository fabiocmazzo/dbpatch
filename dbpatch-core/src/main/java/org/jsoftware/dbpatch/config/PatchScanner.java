package org.jsoftware.dbpatch.config;

import org.jsoftware.dbpatch.impl.DuplicatePatchNameException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Scans for patch and roll-back patch files
 */
public interface PatchScanner extends Serializable {

    /**
     * @param directory base dir
     * @param paths to scan (add directory if not absolute)
     * @return list of found patches
     * @throws DuplicatePatchNameException when duplicated patch names were found
     * @throws IOException io problem
     */
    List<Patch> scan(File directory, String[] paths) throws DuplicatePatchNameException, IOException;

    /**
     * @param patch rollbacks for this patch
     * @param directory base dir
     * @param paths to scan (add directory if not absolute)
     * @return null if no file was found
     * @throws DuplicatePatchNameException when duplicated patch names were found
     * @throws IOException io problem
     */
    File findRollbackFile(File directory, String[] paths, Patch patch) throws DuplicatePatchNameException, IOException;
}
