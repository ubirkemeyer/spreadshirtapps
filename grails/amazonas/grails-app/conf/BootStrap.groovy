import net.sprd.groovy.amazonas.User
import net.sprd.groovy.amazonas.Role
import net.sprd.groovy.amazonas.UserRole
import net.sprd.groovy.amazonas.AmazonasConfig

class BootStrap {
  def springSecurityService

  def init = { servletContext ->

    /*
    //dev only

    def rolesNecessary = ['ROLE_USER', 'ROLE_ADMIN']
    rolesNecessary.each {
      def auth = Role.findByAuthority(it)
      if (!auth) {
        log.info("Add Role $it")
        auth = new Role()
        auth.authority = it
        auth.save()
      } else {
        log.info("Role $it already exists.")
      }
    }
    def admin = User.findByUsername('kab')
    println admin
    if (!admin) {
      User a = new User()
      a.username = "kab"
      a.enabled = true
      a.password = springSecurityService.encodePassword('test')
      a.accountExpired = false
      a.accountLocked = false
      a.passwordExpired = false

      Role roleUser = Role.findByAuthority('ROLE_USER')
      Role roleAdmin = Role.findByAuthority('ROLE_ADMIN')

      if (a.save()) {
        log.info("Created missing admin user")
        println UserRole.create(a, roleUser, true)
        println UserRole.create(a, roleAdmin, true)
        log.info("Added admin user to role")

      } else {
        log.info("Failed to save. ${a.errors}")
      }

    }
       */


    def cnf = AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_EXPORT_ENCODING)
    if (!cnf) {
      cnf = new AmazonasConfig()
      cnf.configurationKey = AmazonasConfig.KEY_EXPORT_ENCODING
      cnf.configurationValue = AmazonasConfig.ENCODING_ISO
      cnf.save()
    }
    cnf = AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_AMAZON_FILE_HEADER)
    if (!cnf) {
      cnf = new AmazonasConfig()
      cnf.configurationKey = AmazonasConfig.KEY_AMAZON_FILE_HEADER
      cnf.configurationValue = 'TemplateType=Clothing	Version=1.4	This row for Amazon.com use only.  Do not modify or delete.'
      cnf.save()
    }
    cnf = AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_API_LIMIT)
    if (!cnf) {
      cnf = new AmazonasConfig()
      cnf.configurationKey = AmazonasConfig.KEY_API_LIMIT
      cnf.configurationValue = 100
      cnf.save()
    }
    cnf = AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_MAX_FILESIZE_BYTES)
    if (!cnf) {
      cnf = new AmazonasConfig()
      cnf.configurationKey = AmazonasConfig.KEY_MAX_FILESIZE_BYTES
      cnf.configurationValue = 10485760
      cnf.save()
    }


  }
  def destroy = {
  }
}