class tomcat {
  include users
  
  file { "/tmp/apache-tomcat-7.0.22.tar.gz":
    source => "puppet:///modules/tomcat/apache-tomcat-7.0.22.tar.gz",
    require => [Exec["${motechUser} homedir"],Exec["sun_jdk_6"]]
  }

  exec { "tomcat_untar":
    command => "tar xfz /tmp/apache-tomcat-7.0.22.tar.gz",
    user => "${motechUser}",
    cwd     => "/home/${motechUser}",
    creates => "/home/${motechUser}/apache-tomcat-7.0.22",
    path    => ["/bin"],
    require => File["/tmp/apache-tomcat-7.0.22.tar.gz"],
  }

  file { "/etc/init.d/tomcat" :
  	content => template("tomcat/tomcat.initd"),
  	mode   =>  777,
  	group  => "root",
  	owner  => "root",
  	require => Exec["tomcat_untar"],
  }

  exec { "installtomcatservice" :
  	command => "/sbin/chkconfig --add tomcat",
  	user => "root", 
  	require => File["/etc/init.d/tomcat"],
  }
  
  service { "tomcat":
  	ensure => running,
  	enable => true,
  	hasstatus => false,
  	require => Exec["installtomcatservice"],
  }
}