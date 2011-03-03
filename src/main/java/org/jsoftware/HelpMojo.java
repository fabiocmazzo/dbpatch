package org.jsoftware;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;



/**
 * Display help
 * @goal help
 */
public class HelpMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		InputStream in = getClass().getResourceAsStream("/dbpatch-help.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s;
			while((s = br.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} finally {
			try {
				in.close();
			} catch(Exception e) { /* ignore */ }
		}
	}
	
}