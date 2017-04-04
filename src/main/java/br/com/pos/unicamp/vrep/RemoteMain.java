package br.com.pos.unicamp.vrep;

import br.com.pos.unicamp.vrep.exceptions.VRepClientException;

public class RemoteMain {

	public static ProxyVRep proxy = new ProxyVRep();

	public static PioneerP3DXCommands ROBOT = new PioneerP3DXCommands();

	public static void main(String[] args) {

		try {

			// Conecta no servidor remoto do VREP

			ROBOT.actuation();

		} catch (VRepClientException e) {
			System.out.println(e.getMsg());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
