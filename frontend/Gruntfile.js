module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    concat: {
		options: {
			separator: ';',
		},
		dist: {
			src: 'js/appMain/*.js',
			dest: 'build/<%= pkg.name %>_init.js',
		},
	},
	uglify: {
		options: {
			banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
		},
		dist:{
			files:{
				'build/<%= pkg.name %>_init.min.js':['<%= concat.dist.dest %>']
			}
		}
    }
  });

  // 加载包含 "uglify" 任务的插件。
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');

  // 默认被执行的任务列表。
  grunt.registerTask('default', ['concat','uglify']);

};