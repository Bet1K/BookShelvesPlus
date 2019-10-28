#!/usr/bin/env bash

case "$(echo "${SHELL}" | sed -E 's|/usr(/local)?||g')" in
	"/bin/zsh")
		RCPATH="${HOME}/.zshrc"
		SOURCE="${BASH_SOURCE[0]:-${(%):-%N}}"
	;;

	*)
		RCPATH="${HOME}/.bashrc"
		if [[ -f "${HOME}/.bash_aliases" ]]; then
			# shellcheck disable=SC2034
			RCPATH="${HOME}/.bash_aliases"
		fi
		SOURCE="${BASH_SOURCE[0]}"
	;;
esac

while [ -h "${SOURCE}" ]; do
	DIRECTORY="$(cd -P "$(dirname "${SOURCE}")" && pwd)"
	SOURCE="$(readlink "${SOURCE}")"
	if [[ "${SOURCE}" != /* ]]; then
		SOURCE="${DIRECTORY}/${SOURCE}"
	fi
done

SOURCE=$([[ "${SOURCE}" = /* ]] && echo "${SOURCE}" || echo "${PWD}/${SOURCE#./}")
base_folder=$(dirname "${SOURCE}")

case "${1}" in
	"download")
	(
		if [ -z "${2}" ]; then
			echo "You have to specify Minecraft version."
		else
			set -e
			cd "${base_folder}"

			mkdir -p "server/plugins"

			curl "https://papermc.io/api/v1/paper/${2}/latest/download" --output "server/Paper.jar"

			echo "Paper downloaded."
		fi
	)
	;;

	"copy")
	(
		set -e
		cd "${base_folder}"

		./gradlew

		cd "${base_folder}/build/libs"

		plugin_name=$(grep < "${base_folder}/settings.gradle.kts" "rootProject.name =" | cut -d ' ' -f 3 | tr -d \" | head -1)
		plugin_version=$(grep < "${base_folder}/build.gradle.kts" "version =" | cut -d ' ' -f 3 | tr -d \" | head -1)
		plugin_has_shadow=$(grep < "${base_folder}/build.gradle.kts" "id(\"com.github.johnrengelman.shadow\")" | head -1)
		if [ -z "${plugin_has_shadow}" ]; then
			plugin_file="${plugin_name}-${plugin_version}.jar"
		else
			plugin_file="${plugin_name}-${plugin_version}-all.jar"
		fi

		echo "Copying jar ${plugin_file}..."
		cp "${plugin_file}" "../../server/plugins"
		echo "Jar copied."
	)
	;;

	"start")
	(
		set -e
		cd "${base_folder}/server"
		java -Dcom.mojang.eula.agree=true -jar "Paper.jar"
	)
	;;

	"clean")
	(
		set -e
		cd "${base_folder}"
		rm -rf "server"
		mkdir "server"
		mkdir "server/plugins"
		echo "Cleaned server files."
	)
	;;

	*)
		echo "test server tool command. This provides a variety of commands to build and manage the test server"
		echo "View below for details of the available commands."
		echo ""
		echo "Commands:"
		echo "  * download <Minecraft version>     | Downloads latest version of Paper for specified release of Minecraft."
		echo "  * copy                             | Compiles and copies plugin build to server."
		echo "  * start                            | Starts server."
		echo "  * clean                            | Cleans server files."
	;;
esac

unset RCPATH
unset SOURCE
unset base_folder
