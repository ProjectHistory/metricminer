package org.metricminer.infra.executor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

public class SimpleCommandExecutor implements CommandExecutor {

	private List<EnvironmentVar> vars = null;
	private static Logger LOG = Logger.getLogger(SimpleCommandExecutor.class);
	
	public void setEnvironmentVar(String name, String value) {
		if (vars == null)
			vars = new ArrayList<EnvironmentVar>();

		vars.add(new EnvironmentVar(name, value));
	}

	public String execute(String command, String basePath) {
		String finalCommand = command;
		Process proc;
		int exitValue = 0;
		String stdout = "";
		String stderr = "";
		try {
			LOG.info("Executing: " + command);
			proc = Runtime.getRuntime().exec(finalCommand, getEnvTokens(),
					new File(basePath));
			stdout = readIs(proc.getInputStream());
			exitValue = proc.waitFor();
			stderr = readIs(proc.getErrorStream());
			LOG.info("Finished with exit value " + exitValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (exitValue != 0) {
			LOG.error("Command " + command + " exited with status " + exitValue);
			LOG.error("stderr: " + stderr);
		}
		return stdout;

	}

	private String readIs(InputStream is) {
		StringBuffer total = new StringBuffer();
		Scanner sc = new Scanner(is);
		while (sc.hasNextLine()) {
			String nextLine = sc.nextLine();
			total.append(nextLine + "\r\n");
		}
		sc.close();
		return total.toString();
	}

	private String[] getEnvTokens() {
		if (vars == null)
			return null;

		String[] envTokenArray = new String[vars.size()];
		Iterator<EnvironmentVar> envVarIter = vars.iterator();
		int nEnvVarIndex = 0;
		while (envVarIter.hasNext() == true) {
			EnvironmentVar envVar = (EnvironmentVar) (envVarIter.next());
			String envVarToken = envVar.fName + "=" + envVar.fValue;
			envTokenArray[nEnvVarIndex++] = envVarToken;
		}

		return envTokenArray;
	}

}

class EnvironmentVar {
	public String fName = null;
	public String fValue = null;

	public EnvironmentVar(String name, String value) {
		fName = name;
		fValue = value;
	}
}
